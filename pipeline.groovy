pipeline {
    agent any

    options {
        timeout(time: 1, unit: 'HOURS') 
    }

    stages {
        stage('Build Package'){
        agent{
            docker {
                image 'markturn'
                args '--user root'
            }          
        }

        steps {
            sh 'cat /etc/os-release'
            sh 'curl -L https://github.com/UAComputerScience/devops-f20-final-exam-yp16.git'
            sh 'mv devops-f20-final-exam-yp16-1.0.0 /Source'
            sh 'mkdir /Build;'
            sh 'cd /Build; cmake /Source -DLINK_STATIC=OFF -G Ninja'
            sh 'cd /Build; ninja'
            sh 'cd /Build; ctest'
            sh 'cd /Build; cpack -G DEB'   
        }
        }
    }
    post {
        success{
           archiveArtifacts artifacts: 'build/*.deb', onlyIfSuccessful: true
        }
    }
}
