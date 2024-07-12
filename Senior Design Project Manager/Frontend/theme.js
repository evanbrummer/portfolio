import { createTheme } from '@mui/material/styles';



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
    spacing: 8, // This sets the base spacing unit for the theme
    overrides: {
        MuiGrid: {
            container: {
                margin: '20px', // Apply a margin of 20px to all Grid containers
            },
            MuiTab: {
              root: {
                textAlign: 'left',
                textDecoration: 'none', // Removing underline from links
              },
            },
        },
    },
    components: {
        MuiPaper: {
          styleOverrides: {
            root: {
              marginTop: 10, // Apply margin-top globally to Paper components used within TableContainer
            },
          },
        },
        MuiTable: {
          styleOverrides: {
            root: {
              minWidth: 100, // Set minimum width globally for all Table components
            },
          },
        },
      },
    
});
export default theme;