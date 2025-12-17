pipeline {
    agent any
    
    tools {
        // These names must match what you typed in Manage Jenkins > Tools
        jdk '21'
        maven '3.8.9'
    }

    triggers {
        // Runs at 5:00 PM London time daily
        cron('0 17 * * *')
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }
        
        stage('Run BDD Tests') {
            steps {
                // This runs your parallel TestNG suite from your YAML
                sh 'mvn clean verify -Dsurefire.suiteXmlFiles=testngParallel.xml'
            }
        }
    }

    post {
        always {
            // This grabs your Extent Reports and Screenshots for viewing in Jenkins
            archiveArtifacts artifacts: 'target/reports/**/*.html, target/reports/**/*.xml, target/reports/**/*.json, target/reports/**/*.png, target/surefire-reports/**/*.xml, target/surefire-reports/**/*.html', allowEmptyArchive: true
        }
    }
}