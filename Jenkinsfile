pipeline {
    agent any
    tools {
        jdk '21' 
        maven '3.8.9' // Ensure this is defined in Manage Jenkins > Tools
    }
    stages {
        stage('Build & Test') {
            steps {
                // withMaven provides better integration for reporting and environment variables
                withMaven(maven: '3.8.9') {
                    sh 'mvn clean verify -Dsurefire.suiteXmlFiles=testngParallel.xml -Dheadless=true'
                }
            }
        }
        stage('Archive Reports') {
            steps {
                archiveArtifacts artifacts: 'target/reports/**/*, target/surefire-reports/**/*.xml', allowEmptyArchive: true
                junit 'target/surefire-reports/*.xml'
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}