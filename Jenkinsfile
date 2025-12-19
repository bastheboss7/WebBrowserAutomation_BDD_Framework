pipeline {
    agent {
        docker {
            image 'markhobson/maven-chrome:jdk-21'
            // We run as root inside the container to have full control
            args '-u root'
        }
    }

    stages {
        stage('Build & Test') {
            steps {
                sh '''
                    mvn clean verify -Dsurefire.suiteXmlFiles=testngParallel.xml -Dheadless=true
                    
                    # THE FIX: This transfers file ownership from the 'root' user 
                    # back to the 'jenkins' user so the artifacts can be saved.
                    chown -R 1000:1000 target/
                '''
            }
        }
    }

    post {
        always {
            // This saves your Cucumber and Extent reports in Jenkins
            archiveArtifacts artifacts: 'target/reports/**/*', allowEmptyArchive: true
            // This displays your TestNG results in a nice graph
            junit 'target/surefire-reports/*.xml'
        }
        cleanup {
            // This wipes the workspace to keep the server clean for the next run
            cleanWs()
        }
    }
}