pipeline {
    agent {
        docker {
            image 'markhobson/maven-chrome:jdk-21'
            // '--rm' ensures the container is removed after use
            // '-u root' prevents permission issues in 'target' folders
            args '-u root --rm'
        }
    }
    stages {
        stage('Test') {
            steps {
                sh 'mvn clean verify -Dsurefire.suiteXmlFiles=testngParallel.xml -Dheadless=true'
            }
        }
    }
    // This post block MUST be at the same level as the agent definition
    post {
        always {
            // We use the script block to handle errors gracefully
            script {
                try {
                    archiveArtifacts artifacts: 'target/reports/**/*', allowEmptyArchive: true
                    junit 'target/surefire-reports/*.xml'
                } catch (Exception e) {
                    echo "Could not archive artifacts: ${e.message}"
                }
            }
        }
        cleanup {
            // This is the most stable place for workspace cleanup
            cleanWs()
        }
    }
}