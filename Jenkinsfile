pipeline {
  environment {
    imagename = "wwx2/social-media-app"
    dockerImage = ''
  }
  agent any
  stages {
    stage('Fix Durable plugin bug') {
      steps {
        System.setProperty("org.jenkinsci.plugins.durabletask.BourneShellScript.HEARTBEAT_CHECK_INTERVAL", "3800");
      }
    }
  stages {
    stage('Cloning Git') {
      steps {
        git([url: 'https://github.com/ons-ou/social-media-backend.git', branch: 'main'])
 
      }
    }
    stage('Building image') {
      steps{
        script {
          dockerImage = docker.build imagename
        }
      }
    }
    stage('Deploy Image') {
      steps{
        script {
          docker.withRegistry( '', registryCredential ) {
            dockerImage.push("$BUILD_NUMBER")
             dockerImage.push('latest')
          }
        }
      }
    }
    stage('Remove Unused docker image') {
      steps{
        sh "docker rmi $imagename:$BUILD_NUMBER"
         sh "docker rmi $imagename:latest"
 
      }
    }
  }
}
