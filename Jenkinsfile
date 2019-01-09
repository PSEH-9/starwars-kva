node {
	def mvnHome
	stage('Checkout Code') {
		echo 'Initiating build using JenkinsFile from Source code'
		git 'https://github.com/PSEH-9/starwars-kva.git'
		echo 'Source code checked out'
		mvnHome = tool 'M3'
	}

	stage('MAVEN Build'){
		echo 'Initiating MAVEN build'
		 if (isUnix()) {
		 	echo 'OS is Unix'
		 	 sh "'${mvnHome}/bin/mvn' clean package -Dmaven.test.skip=true"
		 } else{
		 	echo 'OS is windows'
		 	 bat(/"${mvnHome}\bin\mvn" clean package -Dmaven.test.skip=true/)
		 }
		echo 'MAVEN build done'
	}
	
	stage('Run Tests'){
		echo 'Run Tests'
		 if (isUnix()) {
		 	echo 'OS is Unix'
		 	 sh "'${mvnHome}/bin/mvn' test"
		 } else{
		 	echo 'OS is windows'
		 	 bat(/"${mvnHome}\bin\mvn" test/)
		 }
		echo 'Run Tests done'
	}
	
	stage('Test Results') {
      junit '**/target/surefire-reports/TEST-*.xml'
   }
   
   stage('Deploy') {
   		echo 'Deploy App'
   		if (isUnix()) {
   			sh "chmod 400 jenkins.pem"
   			
   			// Kill Running Application
   			sh "ssh -i jenkins.pem ubuntu@18.216.165.122 sh /home/ubuntu/kva/stop.sh"
   			echo 'Stop Application script executed'
   			
   			//Copy Application Jar
   			sh "scp -r -i jenkins.pem target/starwars-kva-0.0.1-SNAPSHOT.jar  ubuntu@18.216.165.122:/home/ubuntu/kva/"
   			echo 'Application JAR copied'
   			
   			//Start Application
   			sh "ssh -i jenkins.pem ubuntu@18.216.165.122 sh /home/ubuntu/kva/start.sh"
   			echo 'Start Application script executed'
   			echo 'Deployment Complete'
   		} else{
   			echo 'Skipping deployment on windows platform'
   			echo '${WORKSPACE}'
   			bat 'copy /Y target\\*.jar c:\\'
   			echo 'Copied JAR file to C:'
   		}
   }

}