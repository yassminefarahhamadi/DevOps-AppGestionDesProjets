
pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    environment {
        DOCKERHUB_USER = 'yassmine2004'
        DB_IMAGE       = "${DOCKERHUB_USER}/projets-db"
        BACKEND_IMAGE  = "${DOCKERHUB_USER}/projets-backend"
        FRONTEND_IMAGE = "${DOCKERHUB_USER}/projets-frontend"
        TAG            = "${BUILD_NUMBER}"
    }

    stages {

        stage('1 - Get code from Git') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/yassminefarahhamadi/DevOps-AppGestionDesProjets.git'
            }
        }

        stage('2 - Maven Compile') {
            steps {
                dir('backend') {
                    sh 'mvn clean compile'
                }
            }
        }

        stage('3 - Maven Test + JaCoCo') {
            steps {
                dir('backend') {
                    sh 'mvn test'
                }
            }

            post {
                always {
                    junit(
                        allowEmptyResults: true,
                        testResults: 'backend/target/surefire-reports/*.xml'
                    )

                    publishHTML(target: [
                        reportName: 'JaCoCo Coverage Report',
                        reportDir: 'backend/target/site/jacoco',
                        reportFiles: 'index.html',
                        keepAll: true,
                        alwaysLinkToLastBuild: true,
                        allowMissing: true
                    ])
                }
            }
        }

        stage('4 - SonarQube') {
            steps {
                dir('backend') {
                    withSonarQubeEnv('SonarQube') {
                        sh 'mvn sonar:sonar -Dsonar.projectKey=projets-backend'
                    }
                }
            }
        }

        stage('5 - Maven Package') {
            steps {
                dir('backend') {
                    sh 'mvn package -DskipTests'
                }
            }
        }

        stage('6 - Maven Deploy (Nexus)') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'nexus-creds',
                        usernameVariable: 'NEXUS_USER',
                        passwordVariable: 'NEXUS_PASS'
                    )
                ]) {
                    sh '''
                        set +x

                        cat > settings-nexus.xml <<EOF
<settings>
  <servers>
    <server>
      <id>nexus</id>
      <username>${NEXUS_USER}</username>
      <password>${NEXUS_PASS}</password>
    </server>
  </servers>
</settings>
EOF

                        cd backend
                        mvn deploy -DskipTests -s ../settings-nexus.xml
                    '''
                }
            }
        }

        stage('7 - Docker Images (login + push)') {
            steps {
                sh '''
                    docker build -t $DB_IMAGE:$TAG -t $DB_IMAGE:latest ./db
                    docker build -t $BACKEND_IMAGE:$TAG -t $BACKEND_IMAGE:latest ./backend
                    docker build -t $FRONTEND_IMAGE:$TAG -t $FRONTEND_IMAGE:latest ./frontend
                '''

                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-creds',
                        usernameVariable: 'DH_USER',
                        passwordVariable: 'DH_PASS'
                    )
                ]) {
                    sh '''
                        set +x
                        echo "$DH_PASS" | docker login -u "$DH_USER" --password-stdin

                        docker push $DB_IMAGE:$TAG
                        docker push $DB_IMAGE:latest

                        docker push $BACKEND_IMAGE:$TAG
                        docker push $BACKEND_IMAGE:latest

                        docker push $FRONTEND_IMAGE:$TAG
                        docker push $FRONTEND_IMAGE:latest
                    '''
                }
            }
        }

        stage('8 - Docker Compose Up') {
            steps {
                sh '''
                    docker compose down || true
                    docker compose up -d
                    docker compose ps
                '''
            }
        }
    }

    post {
        always {
            sh 'docker logout || true'
            sh 'rm -f settings-nexus.xml'
        }
    }
}
