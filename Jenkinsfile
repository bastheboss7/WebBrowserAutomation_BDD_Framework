pipeline {
    agent {
        docker {
            image 'markhobson/maven-chrome:jdk-21'
            args '-u root'
        }
    }
    // NOTICE: No "tools" block here! The image handles it.
    stage('Build & Test') {
    steps {
        sh '''
            mvn clean verify -Dsurefire.suiteXmlFiles=testngParallel.xml -Dheadless=true
            
            # THE FIX: Give ownership back to the Jenkins user (ID 1000) 
            # so Jenkins can archive the artifacts.
            chown -R 1000:1000 target/
        '''
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