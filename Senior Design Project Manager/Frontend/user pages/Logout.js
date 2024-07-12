import React, {useContext, useEffect} from 'react';
import { useNavigate } from 'react-router-dom';
import ScreenLoader from '../components/ScreenLoader';
import { UserContext } from '../UserContext';
import { Link } from 'react-router-dom';
import '../css/index.css'
import { ThemeProvider } from '@emotion/react';
import { Button, createTheme } from '@mui/material';

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

export default function Logout() {

    const navigate = useNavigate();
    const { user, setUser } = useContext(UserContext);

    //todo: fetch call to unregister cookie or whatever we're using in the backend
    sessionStorage.removeItem("USER");
    setUser(null);

    useEffect(() => {
        if (user == null) {
            //navigate('/');
        }
    }, [user]);

    return (
        <ThemeProvider theme={theme}>
            {user ? (
                <ScreenLoader />
            ) : (
                <div className='dead-center'>
                    <div style={{ display: 'block', textAlign: 'center' }}>
                        <p style={{ fontFamily: 'Inter', fontWeight: 450}}>Logged out successfully.</p>
                        <Button href='/'>Back to login</Button>
                    </div>
                </div>
            )}
        </ThemeProvider>
    )
}