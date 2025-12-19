pipeline {
    agent {
        docker {
            image 'markhobson/maven-chrome:jdk-21'
            // This flag is often needed to run Docker-in-Docker or for Chrome permissions
            args '-u root' 
        }
    }
    stages {
        stage('Environment Check') {
            steps {
                // Verify that Chrome and Java are actually there
                sh 'java -version'
                sh 'mvn -version'
                sh 'google-chrome --version'
            }
        }
        stage('Build & Test') {
            steps {
                // We use 'sh' directly because Maven is already in the Docker image
                sh 'mvn clean verify -Dsurefire.suiteXmlFiles=testngParallel.xml -Dheadless=true'
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