pipeline {

    agent any
    
   /* tools {
        maven "Maven"
    }*/
stages {
    
    stage('git clone') {
        steps {
            git branch: 'main', credentialsId: 'jenkins', url: 'git@gitlab.com:Aguerbaoui/projet_j2e.git'
        }
    }

    stage('Build') {
        steps {
            withMaven(maven: 'Maven') {
                sh 'mvn clean install package'
            }
        }
    }

    stage("Publish to Nexus Repository Manager") {
            steps {
                script {
                    pom = readMavenPom file: "pom.xml";
                   
                        nexusArtifactUploader(
                            nexusVersion: 'nexus3',
                            protocol: 'http',
                            nexusUrl: '52.53.193.47:8081',
                            groupId:'com.example.maven-project',
                            version: pom.version,
                            repository: 'Project_JEE',
                            credentialsId: 'nexus',
                            artifacts: [
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: artifactPath,
                                type: pom.packaging],
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: "pom.xml",
                                type: 'war']
                            ]
                        );
                    } else {
                        error "*** File: ${artifactPath}, could not be found";
                    }
                }
            }
        }

    stage('Deploy') {
        steps {
            deploy adapters: [tomcat8(credentialsId: 'tomcat', 
            path: '', 
            url: 'http://54.67.30.95:8080/')], 
            contextPath: null, 
            war: '**/*.war'
        }
    }
} 
   
    

    /* post {
        start {
             slackSend message: " Start pipeline: Job ${env.JOB_NAME}, Build Jenkins N°: ${env.BUILD_NUMBER}: -To see more details,Check console output at:${env.BUILD_URL}"

        }
        success {
            emailext body: "${currentBuild.currentResult}: Job ${env.JOB_NAME}\nMore Info can be found here: ${env.BUILD_URL}", recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']], subject: 'Test'
             
        }
    } */

} 

