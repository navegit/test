pipeline {
    agent any
    stages {
        stage('Cleanup Workspace - Master') {
            steps {
                echo 'Cleaning workspace....'
                cleanWs()
            }
        }
    }
}