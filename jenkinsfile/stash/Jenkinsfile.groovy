pipeline {
    agent any
    stages {
        stage('Cleanup Workspace - Master') {
            steps {
                echo 'Cleaning workspace....'
                cleanWs()
            }
        }
        stage('Stash') {
            steps {
                sh '''
                    mkdir -p output
                    cd output
                    touch myoutput
                    mkdir -p test1
                '''

                // Write a text file there.
                writeFile file: "output/test1/somefile", text: "Hey look, some text."

                // Stash that directory and file.
                // Note that the includes could be "output/", "output/*" as below, or even
                // "output/**/*" - it all works out basically the same.
                // dir('my-dir'){
               //    git branch: 'master', url: 'https://github.com/navegit/testproj.git'
                // }
                // stash name: "first-stash", includes: "my-dir/*"
                stash name: "first-stash", includes: "output/**/*"
            }
        }

    }
}