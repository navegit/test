pipeline {
  agent any
  tools {
     maven 'Maven-3.5.4'
  }
  stages {
    stage("Step Level") {
       steps {
           echo "hello"
           // Running Shell script: Parentheses are optional when a single parameter is used
           sh('echo $PATH')
           sh 'echo $PATH'
           sh '''
              echo "Multiline shell steps works too"
              ls -lah
           '''

           //create a directory called "tmp" and cd into that directory
           dir("tmp") {
                sh 'echo "tmp dir"'
                // use git to retrieve the plugin-pom
                /* git changelog: false, poll: false, url: 'git://github.com/jenkinsci/plugin-pom.git', branch: 'master' */
           }

           //Running Scripted pipeline code
           // variable assignment (other than environment variables) can only be done in a script block
           // complex global variables (with properties or methods) can only be run in a script block
           // env variables can also be set within a script block
           script {
             foo = "hello var"
           }
           echo "$foo"

           writeFile text: 'hello world', file: 'msg.out'
           // Some steps may not be fully implemented with symbols or step names to run directly
           // steps can be used to call the class directly and run that step.
           step([$class: 'ArtifactArchiver', artifacts: 'msg.out', fingerprint: true])

           /*
            * Any Pipeline steps and wrappers can be used within the "steps" section
            * of a Pipeline and they can be nested.
            * Refer to the Pipeline Syntax Snippet Generator for the correct syntax of any step or wrapper
            */
           timeout(time: 5, unit: "SECONDS") {
             retry(5) {
               echo "hello"
             }
           }

           //blocks for input and won’t proceed without a person confirming the progress.
           /* input "Does the staging environment look ok?"  */
       }
    }
  }
  post {
    always {
      echo "I ALWAYS run firsts"
    }
    unstable {
      echo "UNSTABLE runs after ALWAYS"
    }
  }
}