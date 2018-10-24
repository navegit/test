pipeline {
    agent any
    stages {
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

                stash name: 'first-stash', includes: 'output/**/*'
                sleep 15
            }
        }
        stage('Unstash') {
            /* steps {
              // dir("first-stash") {
              //         unstash "first-stash"
              // }
                  dir("first-stash") {
                      unstash "first-stash"
                  }

                  // Look, no output directory under the root!
                  // pwd() outputs the current directory Pipeline is running in.

                  sleep 15
           }*/
            parallel {
                stage('Run Integration Tests') {
                    steps {
                        dir('app-test') {
                            unstash 'first-stash'
                            sh "ls -l"
                            writeFile file: "output/test1/somefile", text: "from test2."
                        }
                    }
                }
                stage('Run Unit Tests') {
                    steps {
                        dir('app-test') {
                            unstash 'first-stash'
                            sh "ls -l"
                            writeFile file: "output/test1/somefile", text: "from test1."
                        }
                    }
                }
            }
        }
        stage ('Build Workspace') {
            steps {
                stash name: "first-stash", includes: "output/myoutput"
                sleep 15
            }
        }
        stage('Cleanup Workspace - Slave') {
            steps {
                echo 'Cleaning workspace....'
                cleanWs()
            }
        }
        stage('Deploy Workspace') {
            steps {
                echo "deploying workspace"
                unstash 'first-stash'
            }
        }
    }
}