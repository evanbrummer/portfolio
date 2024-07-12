import React, {useContext} from 'react';
import StudentDash from './student/StudentDash';
import ClientDash from './client/ClientDash';
import AdvisorDash from './faculty advisor/AdvisorDash';
import BoardDash from './board member/BoardDash';
import InstructorDash from './instructor/InstructorDash';
import ScreenLoader from '../components/ScreenLoader';
import { createTheme } from '@mui/material/styles';
import { UserContext } from '../UserContext';
import '../css/index.css'

const theme = createTheme({
    palette: {
      primary: {
        main: "#A71930",
        },
        secondary: {
            main: '#006B2B',
        },
    },
    typography: {
        fontFamily: 'Inter',
    },
});

export default function Dashboard() {

    const { user, type } = useContext(UserContext);
    
    //console.log("Dashboard: type is " + type);

    return (
        type == "Student" ? (
            <StudentDash userType={type} />
          )
            : type == "Client" ? (
            <ClientDash userType={type} />
          )
            : type == "Advisor" ? (
            <AdvisorDash userType={type} />
          ) : type == "Board" ? (
            <BoardDash userType={type} />
          ) : type == "Instructor" ? (
            <InstructorDash userType={type} />
          ) : (
            <ScreenLoader />
          )
    )

}