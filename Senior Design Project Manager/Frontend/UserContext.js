import React, { createContext, useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import ScreenLoader from './components/ScreenLoader';
import { createTheme } from '@mui/material/styles';

const UserContext = createContext();

const theme = createTheme({
    palette: {
      primary: {
        main: "#A71930",
      },
      secondary: {
        main: "#888888"
      }
    },
    typography: {
      fontFamily: 'Inter',
    },
  });

const UserProvider = ({ children }) => {
    const [loading, setLoading] = useState(true);
    const [user, setUser] = useState(null);
    const [type, setType] = useState(null);

    const location = useLocation();
    const navigate = useNavigate();

    useEffect(() => {
        let userFromLocal = sessionStorage.getItem("USER");
        if (userFromLocal == null) {
            console.log("User is null, need to log in.");
            if (location.pathname !== "/") {
                navigate("/"); // FORCE USER BACK TO LOGIN (COMMENT OUT IF INCONVENIENT)
            }
        } else {
            let parsedUser = JSON.parse(userFromLocal)
            setUser(parsedUser);
            setType(parsedUser.userType[0]);
        }
        setLoading(false);
    }, []);

    return (
        <UserContext.Provider value={{ user, setUser, type, setType }}>
            {loading ? (
                <ScreenLoader />
            ) : (
                children
            )}
        </UserContext.Provider>
    );
};

export { UserContext, UserProvider };