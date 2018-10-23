pipeline {
    agent any
    stages {
        stage('Stash') {
            steps {
                sh "mkdir -p output"
                sh "cd output"
                sh "touch myoutput"
                sh "mkdir -p test1"
                sh "cd test1"

                // Write a text file there.
                writeFile file: "output/test1/somefile", text: "Hey look, some text."

                // Stash that directory and file.
                // Note that the includes could be "output/", "output/*" as below, or even
                // "output/**/*" - it all works out basically the same.
                // dir('my-dir'){
               //    git branch: 'master', url: 'https://github.com/navegit/testproj.git'
                // }
                // stash name: "first-stash", includes: "my-dir/*"
                stash name: "first-stash", includes: "output/*"
            }
        }
        stage('Cleanup Workspace - Slave') {
                steps {
                    echo 'Cleaning workspace....'
                    cleanWs()
                }
        }
        stage('Unstash') {
            steps {
               // dir("first-stash") {
               //         unstash "first-stash"
               // }
                   dir("first-stash") {
                       unstash "first-stash"
                   }

                   // Look, no output directory under the root!
                   // pwd() outputs the current directory Pipeline is running in.

                   sleep 15
            }
        }
    }
}