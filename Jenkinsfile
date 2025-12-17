pipeline {
    agent any
    
    tools {
        jdk '21'
        maven '3.8.9'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Run BDD Tests') {
            steps {
                // The 'withMaven' block automatically sets JAVA_HOME and PATH for you
                withMaven(maven: '3.8.9', jdk: '21') {
                    sh 'mvn clean verify -Dsurefire.suiteXmlFiles=testngParallel.xml'
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/reports/**/*.html, target/reports/**/*.xml', allowEmptyArchive: true
        }
    }
}