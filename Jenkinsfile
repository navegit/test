pipeline {
  agent none

  stages {
    stage("Hello") {
      steps {
        echo "hello"
        // need to use script block to assign value
        script {
          echo "HOW R U"
        }
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