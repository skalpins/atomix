pipeline {

  agent {
    kubernetes {
      cloud 'zeebe-ci'
      label "zeebe-ci-build_${env.JOB_BASE_NAME}-${env.BUILD_ID}"
      defaultContainer 'jnlp'
      yaml '''\
apiVersion: v1
kind: Pod
metadata:
  labels:
    agent: zeebe-ci-build
spec:
  nodeSelector:
    cloud.google.com/gke-nodepool: slaves
  tolerations:
    - key: "slaves"
      operator: "Exists"
      effect: "NoSchedule"
  containers:
    - name: maven
      image: maven:3.6.0-jdk-8
      command: ["cat"]
      tty: true
      env:
        - name: JAVA_TOOL_OPTIONS
          value: |
            -XX:+UnlockExperimentalVMOptions
            -XX:+UseCGroupMemoryLimitForHeap
      resources:
        limits:
          cpu: 2
          memory: 8Gi
        requests:
          cpu: 1
          memory: 4Gi
'''
    }
  }

  options {
    buildDiscarder(logRotator(numToKeepStr: '10'))
    timestamps()
    timeout(time: 45, unit: 'MINUTES')
  }

  environment {
    NEXUS = credentials("camunda-nexus")
  }

  stages {
    stage('Prepare') {
      steps {
        container('maven') {
            sh 'mvn clean install -B -s settings.xml -DskipTests -Dmaven.test.skip -Dmaven.javadoc.skip -Ddockerfile.skip'
        }
      }
    }

    stage('Build') {
      steps {
        container('maven') {
            sh 'mvn install -B -s settings.xml -Ddockerfile.skip -Dmaven.test.redirectTestOutputToFile'
        }
      }

      post {
        always {
            junit testResults: "**/*/TEST-*.xml", keepLongStdio: true
        }
      }
    }

    stage('Upload') {
      when { branch 'zeebe' }
      steps {
        container('maven') {
            sh 'mvn -B -s settings.xml generate-sources source:jar javadoc:jar deploy -DskipTests -Ddockerfile.skip'
        }
      }
    }
  }
}