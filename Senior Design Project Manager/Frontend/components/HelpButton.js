import '../css/index.css'
import * as React from 'react';
import { Snackbar, Alert, Button , ThemeProvider } from '@mui/material';
import { createTheme } from '@mui/material/styles';

const theme = createTheme({
    palette: {
      primary: {
        main: "#A71930",
      },
      secondary: {
        main: "#231F20",
      }
    },
    typography: {
        fontFamily: 'Inter',
    },
});

export default function HelpButton() {
    const [state, setState] = React.useState({
        open: false,
        vertical: 'top',
        horizontal: 'center',
    });

    const { vertical, horizontal, open } = state;

    const handleClick = (newState) => () => {
        setState({ open: true, ...newState });
    };

    const handleClose = () => {
        setState({ ...state, open: false });
    };

    return (
        <ThemeProvider theme={theme}>
            <Snackbar anchorOrigin={{ vertical, horizontal }} open={open} onClose={handleClose} key={vertical + horizontal}>
                <Alert severity="error"><strong>For Assistance:</strong> Please email the site admin at sd_admin@iastate.edu</Alert>
            </Snackbar>
            <Button onClick={handleClick({ vertical: 'bottom', horizontal: 'center' })} style={{ fontSize: '17px' }}>Help</Button>
        </ThemeProvider>
    )
};