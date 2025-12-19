pipeline {
    agent {
        docker {
            image 'markhobson/maven-chrome:jdk-21'
            args '-u root'
        }
    }
    // NOTICE: No "tools" block here! The image handles it.
    stages {
        stage('Build & Test') {
            steps {
                // We use 'sh' directly because 'mvn' is inside the markhobson image
                sh 'mvn clean verify -Dsurefire.suiteXmlFiles=testngParallel.xml -Dheadless=true'
                chown -R 1000:1000 target/
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: 'target/reports/**/*', allowEmptyArchive: true
            junit 'target/surefire-reports/*.xml'
        }
        cleanup {
            cleanWs()
        }
    }
}