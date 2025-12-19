pipeline {
    agent {
        docker {
            image 'markhobson/maven-chrome:jdk-21'
            // '-u root' ensures permission to create folders and run Chrome
            // '--entrypoint=' overrides any default image startup logic
            args '-u root --entrypoint='
        }
    }

    // Parameters allow you to run the same job with different settings
    parameters {
        string(name: 'BROWSER', defaultValue: 'chrome', description: 'Browser for testing')
        string(name: 'ENV', defaultValue: 'PROD', description: 'Environment: TEST, STAGING, or PROD')
    }

    stages {
        stage('Environment Audit') {
            steps {
                // Diagnostic checks to prevent "ghost" failures
                sh '''
                    echo "Checking Binaries..."
                    java -version
                    mvn -version
                    google-chrome --version
                '''
            }
        }

        stage('Build & Test') {
            steps {
                // Using 'sh' is better than 'withMaven' when using this specific Docker image
                // as the environment is already pre-configured.
                sh "mvn clean verify -Dsurefire.suiteXmlFiles=testngParallel.xml -Dheadless=true -Denv=${params.ENV}"
            }
        }
    }

    post {
        always {
            // Archive results first before cleaning the workspace
            archiveArtifacts artifacts: 'target/reports/**/*, target/surefire-reports/**/*.xml', allowEmptyArchive: true
            junit 'target/surefire-reports/*.xml'
        }
        success {
            echo 'Build Successful! Test reports are available in the Artifacts tab.'
        }
        failure {
            echo 'Build Failed. Check the archived screenshots in target/reports.'
        }
        cleanup {
            // This ensures the Jenkins Agent disk doesn't fill up over time
            cleanWs()
        }
    }
}