package com.iastate.project_matcher.service;

import com.iastate.project_matcher.algorithm.*;
import com.iastate.project_matcher.constants.Major;
import com.iastate.project_matcher.entity.PreferencesEntity;
import com.iastate.project_matcher.entity.ProjectEntity;
import com.iastate.project_matcher.entity.TeamEntity;
import com.iastate.project_matcher.entity.UserEntity;
import com.iastate.project_matcher.exception.ProjectDoesNotExistException;
import com.iastate.project_matcher.exception.UserDoesNotExistException;
import com.iastate.project_matcher.repository.ProjectRepository;
import com.iastate.project_matcher.repository.TeamRepository;
import com.iastate.project_matcher.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlgorithmService {
    UserService userService;
    ProjectService projectService;
    TeamRepository teamRepository;
    UserRepository userRepository;
    ProjectRepository projectRepository;

    @Autowired
    public AlgorithmService(UserService userService, ProjectService projectService, TeamRepository teamRepository, UserRepository userRepository, ProjectRepository projectRepository) {
        this.userService = userService;
        this.projectService = projectService;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    public List<UserEntity> MatchStudentsToProjects() throws UserDoesNotExistException, ProjectDoesNotExistException {
        List<UserEntity> users = userService.getAllActiveStudents();
        Student[] students = mapUserEntityToAlgoVersion(users);
        Project[] projects = mapProjectsToAlgoVersion(projectService.getActiveProjects());
        Algorithm algorithm = new Algorithm(students, projects);
        students = algorithm.MatchStudentsToProjects();
        List<Integer> neededTeams = new ArrayList<>();
        for (int x = 0; x < users.size(); x++) {
            boolean addNum = true;
            int teamNum = students[x].getProject().getID();
            for (int y = 0; y < neededTeams.size(); y++) {
                if(neededTeams.get(y) == teamNum) {
                    addNum = false;
                }
            }
            if (addNum) {
                neededTeams.add(teamNum);
            }
        }
        for (int x = 0; x < neededTeams.size(); x++) {
            TeamEntity team = new TeamEntity();
            List<UserEntity>teamMembers = new ArrayList<>();
            team.setTeamMembers(teamMembers);
            ProjectEntity project = projectService.getProject(neededTeams.get(x));
            team.setProjectEntity(projectService.getProject(neededTeams.get(x)).getId());
            team.setProjectName(projectService.getProject(neededTeams.get(x)).getProjectName());
            project.getTeams().add(team);
            team.setTeam_number(x + 1);
            team.setCohortName("sdmay24");
            for (int y = 0; y < students.length; y++) {
                if (students[y].getProject().getID() == team.getProjectEntity()) {
                    UserEntity user = userService.getUser(students[y].getID());
                    team.getTeamMembers().add(userService.getUser(students[y].getID()));
                    user.getTeams().add(team);
                }
            }
            projectRepository.save(project);
            teamRepository.save(team);
        }

        return users;
    }

    private Student[] mapUserEntityToAlgoVersion(List<UserEntity> userEntityList) throws ProjectDoesNotExistException {
        Student[] students = new Student[userEntityList.size()];
        for (int x = 0; x < userEntityList.size(); x++) {
            UserEntity userEntity = userEntityList.get(x);
            PreferencesEntity preferencesEntity = userEntity.getPreferencesEntity();
            Preferences preferences = new Preferences(preferencesEntity.getP1(), preferencesEntity.getP2(), preferencesEntity.getP3(), preferencesEntity.getP4(), preferencesEntity.getP5(), preferencesEntity.getS1(),
                    preferencesEntity.getS2(), preferencesEntity.getS3());
            Student student = new Student(userEntity.getId(), userEntity.getMajor(), preferences, null);
            students[x] = student;
        }
        return students;
    }

    private Project[] mapProjectsToAlgoVersion(List<ProjectEntity> projectEntityList) {
        Project[] projects = new Project[projectEntityList.size()];
        for (int x = 0; x < projectEntityList.size(); x++) {
            ProjectEntity projectEntity = projectEntityList.get(x);
            List<Major> majorsList = new ArrayList<>();
            if (projectEntity.getCyber()) {
                majorsList.add(Major.CYBE);
            }
            if (projectEntity.getSe()) {
                majorsList.add(Major.SE);
            }
            if (projectEntity.getEe()) {
                majorsList.add(Major.EE);
            }
            if (projectEntity.getCpre()) {
                majorsList.add(Major.CPRE);
            }
            Project project = new Project(projectEntity.getId(), projectEntity.getNumPeople(), majorsList.toArray(new Major[0]));
            projects[x] = project;
        }
        return projects;
    }

    private Major[] getMajorsArray(ProjectEntity project) {
        List<Major> majors = new ArrayList<Major>();
        if (project.getSe()) {
            majors.add(Major.SE);
        }
        if (project.getEe()) {
            majors.add(Major.EE);
        }
        if (project.getCpre()) {
            majors.add(Major.CPRE);
        }
        if (project.getCyber()) {
            majors.add(Major.CYBE);
        }
        return majors.toArray(new Major[majors.size()]);
    }
}
