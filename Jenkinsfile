pipeline {
  agent any

tools {
    maven "Maven3"
}
  stages {
    stage('Checkout') {
      steps {
        git url: 'https://github.com/Madhanvelu101/crypto-currency-converter.git', branch: 'master'
      }
    }

    stage('Build') {
      steps {
        sh 'mvn clean package'
      }
    }

    stage('Test') {
      steps {
        sh 'mvn test'
      }
    }

    stage('Deploy') {
      steps {
        sh 'mvn deploy'
      }
    }
  }
}
