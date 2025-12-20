pipeline {
    agent {
        docker {
            image 'markhobson/maven-chrome:jdk-21'
            // We run as root inside the container to have full control
            args '-u root'
        }
    }

    // This creates the dropdowns in the Jenkins UI
    parameters {
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'], description: 'Pick a browser')
        choice(name: 'ENVIRONMENT', choices: ['QA', 'UAT', 'PROD'], description: 'Target Environment')
        choice(name: 'TAGS', choices: ['@ParcelDelivery', '@ProhibitedItems', '@ParcelShopFilter'], description: 'Cucumber tags to run')
    }

    stages {
        stage('Build & Test') {
            steps {
                sh """
                    mvn clean verify \
                    -Dbrowser=${params.BROWSER} \
                    -Denv=${params.ENVIRONMENT} \
                    -Dcucumber.filter.tags="${params.TAGS}"
                    
                    # Ownership fix
                    chown -R 1000:1000 target/
                """
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
                reportDir: 'target/reports/cucumber-report/cucumber-html-reports', 
                // Use the correct filename you found
                reportFiles: 'overview-features.html',
                reportName: 'Cucumber HTML Report'         // The name of the tab in Jenkins
            ])

            junit 'target/surefire-reports/*.xml'
        }
        cleanup {
            cleanWs()
        }
    }
}