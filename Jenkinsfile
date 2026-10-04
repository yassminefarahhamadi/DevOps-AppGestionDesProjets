pipeline {
    agent any

    tools { maven 'Maven3' }

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
                dir('backend') { sh 'mvn clean compile' }
            }
        }

        stage('3 - SonarQube') {
            steps {
                dir('backend') {
                    withSonarQubeEnv('SonarQube') {
                        sh 'mvn verify sonar:sonar -Dsonar.projectKey=projets-backend -Dsonar.token=$SONAR_AUTH_TOKEN'
                    }
                }
            }
        }

        stage('4 - Maven Test') {
            steps {
                dir('backend') { sh 'mvn test' }
            }
            post {
                always { junit allowEmptyResults: true, testResults: 'backend/target/surefire-reports/*.xml' }
            }
        }

        stage('5 - Maven Package') {
            steps {
                dir('backend') { sh 'mvn package -DskipTests' }
            }
        }

        stage('6 - Maven Deploy') {
            steps {
                dir('backend') { sh 'mvn deploy -DskipTests -Dmaven.deploy.skip=true' }
            }
        }

        stage('7 - Docker Images (login + push)') {
            steps {
                sh '''
                    docker build -t $DB_IMAGE:$TAG -t $DB_IMAGE:latest ./db
                    docker build -t $BACKEND_IMAGE:$TAG -t $BACKEND_IMAGE:latest ./backend
                    docker build -t $FRONTEND_IMAGE:$TAG -t $FRONTEND_IMAGE:latest ./frontend
                '''
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds',
                                 usernameVariable: 'DH_USER', passwordVariable: 'DH_PASS')]) {
                    sh '''
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
        always { sh 'docker logout || true' }
    }
}
