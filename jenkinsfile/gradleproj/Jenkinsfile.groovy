pipeline {
    agent any
    tools {
        gradle "Gradle-3.4"
    }
    stages {
        stage('Cleanup Workspace - Master') {
            steps {
                echo 'Cleaning workspace....'
                cleanWs()
            }
        }
        stage('Checkout') {
            steps {
                dir('app'){
                    git branch: 'test-branch', url: 'https://github.com/navegit/testproj.git'
                }
                sleep 15
            }
        }
        stage('Build workspace') {
            steps {
              dir('app') {
                  sh 'gradle clean build'
              }
              echo "stashing"
              stash name: "app", includes: "app/build/libs/*.jar"
              sleep 15
            }
        }
        stage('Cleanup Workspace') {
            steps {
                echo 'Cleaning workspace....'
                cleanWs()
            }
        }
        stage('Deploy Workspace') {
            steps {
               echo 'Unstashing workspace....'
               unstash "app"
               sleep 15
            }
        }
    }
}