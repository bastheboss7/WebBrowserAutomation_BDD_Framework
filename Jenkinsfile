pipeline {
    agent any
    tools {
        // These names MUST match the names you gave in "Manage Jenkins > Tools"
        jdk '21' 
        maven '3.8.9'
    }
    stages {
        stage('Build') {
            steps {
                // withMaven will now use the JAVA_HOME set by the tools block
                withMaven(maven: '3.8.9') {
                    sh 'mvn clean install'
                }
            }
        }
    }
}