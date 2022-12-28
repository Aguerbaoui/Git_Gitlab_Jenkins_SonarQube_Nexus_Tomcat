@Library('slack_demo') _
pipeline {

    agent any
    stages {
    
    stage('git clone') {
        steps {
        git branch: 'main', credentialsId: 'jenkins', url: 'git@gitlab.com:formation-101222/projet_j2e.git'
        }
    }

    
    stage('Build') {
        steps {
            withMaven(maven: 'MAVEN') {
                sh 'mvn clean install package'
            }
        }
    }
    
    stage('Deploy') {
        steps {
            deploy adapters: [tomcat8(credentialsId: 'tomcat', 
            path: '', 
            url: 'http://13.37.180.37:8080/')], 
            contextPath: null, 
            war: '**/*.war'
            }
    }
    } 
    post { 
          always { 

            sendNotification currentBuild.result

            }
      }    
    
}