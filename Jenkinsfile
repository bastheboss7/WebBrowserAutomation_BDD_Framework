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
            // 1. Keep the standard archive for downloading
            archiveArtifacts artifacts: 'target/reports/**/*', allowEmptyArchive: true
            
            // 2. The NEW Step: Publish the report as a tab in Jenkins
            publishHTML([
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'target/reports/cucumber-report', // Path to your report folder
                reportFiles: 'index.html',                 // The main HTML file
                reportName: 'Cucumber HTML Report'         // The name of the tab in Jenkins
            ])

            junit 'target/surefire-reports/*.xml'
        }
        cleanup {
            cleanWs()
        }
    }
}