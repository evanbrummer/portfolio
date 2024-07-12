package com.iastate.project_matcher;

import com.github.javafaker.Faker;
import com.iastate.project_matcher.constants.Major;
import com.iastate.project_matcher.constants.UserType;
import com.iastate.project_matcher.entity.*;
import com.iastate.project_matcher.repository.ProjectRepository;
import com.iastate.project_matcher.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

@SpringBootApplication
public class Application {

	@Autowired
	UserRepository userRepository;
	@Autowired
	ProjectRepository projectRepository;
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner() {
		return args -> {

			// add an instructor no matter what
			UserEntity firstInstructor = new UserEntity();
			firstInstructor.addUserType(UserType.Instructor);
			userRepository.save(firstInstructor);

			int max = 300;
			Faker faker = new Faker(new Locale("en-US"));
			if (Objects.equals(System.getenv("DB_SEED"), "true")) {
				UserEntity userEntity = new UserEntity();
				userEntity.setFirstName(faker.name().firstName());
				userEntity.setLastName(faker.name().lastName());
				userEntity.setUsername(faker.name().username());
				userEntity.setIsActive(true);
				userEntity.setEmail(faker.name().firstName() + faker.name().bloodGroup() + "redacted");
				userEntity.addUserType(UserType.Instructor);
				userRepository.save(userEntity);
				for (int i = 0; i <= max; i++) {
					UserEntity user = new UserEntity();
					PreferencesEntity preferencesEntity =  new PreferencesEntity();

					user.setFirstName(faker.name().firstName());
					user.setLastName(faker.name().lastName());
					user.setUsername(faker.name().username());
			
					user.setSectionNumber(3);
					Major[] majors = Major.values();
					Random rand = new Random();
					int index = rand.nextInt(majors.length);
					Major randomMajor = majors[index];
					user.setMajor(randomMajor);
					user.setIsActive(true);
					user.setHasSubmitted(false);
					user.setEmail(faker.name().firstName() + faker.name().bloodGroup() + "redacted");
					user.addUserType(UserType.Student);
					user.setPreferencesEntity(preferencesEntity);
					userRepository.save(user);
				}
				Random rand = new Random();
				for(int i = 0; i < 65; i++) {
					ProjectEntity project = new ProjectEntity();
					project.setSe(true);
					project.setEe(true);
					project.setCpre(true);
					project.setCyber(true);
					project.setIsAvailable(true);
					project.setIsApproved(true);
					int numPeople = rand.nextInt(4) + 4;
					project.setNumPeople(numPeople);
					project.setProjectName(faker.elderScrolls().city());
					project.setProjectDescription(faker.elderScrolls().quote());
					project.setClientName(faker.elderScrolls().race());
					projectRepository.save(project);
				}
				List<UserEntity> users = userRepository.findAll();
				List<ProjectEntity> projects = projectRepository.findAll();
				for (int i = 0; i < users.size(); i++) {
					UserEntity user = users.get(i);
					int maxLimit = 13;
					int maxBid = 10;
					Random projectRand = new Random();
					Random bidRand = new Random();
					if (user.hasUserType(UserType.Student)) {
						PreferencesEntity preferencesEntity = user.getPreferencesEntity();
						preferencesEntity.setP1(projects.get(projectRand.nextInt(maxLimit)).getId());
						preferencesEntity.setP2(projects.get(projectRand.nextInt(maxLimit)).getId());
						preferencesEntity.setP3(projects.get(projectRand.nextInt(maxLimit)).getId());
						preferencesEntity.setP4(projects.get(projectRand.nextInt(maxLimit)).getId());
						preferencesEntity.setP5(projects.get(projectRand.nextInt(maxLimit)).getId());

						preferencesEntity.setBidP1(bidRand.nextInt(maxBid));
						preferencesEntity.setBidP2(bidRand.nextInt(maxBid));
						preferencesEntity.setBidP3(bidRand.nextInt(maxBid));
						preferencesEntity.setBidP4(bidRand.nextInt(maxBid));
						preferencesEntity.setBidP5(bidRand.nextInt(maxBid));

						user.setPreferencesEntity(preferencesEntity);
						userRepository.save(user);
					}
				}
			}
		};
	}
}
