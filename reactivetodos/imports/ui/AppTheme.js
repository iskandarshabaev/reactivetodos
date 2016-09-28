import {deepPurple500, lightBlue500} from 'material-ui/styles/colors';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import getMuiTheme from 'material-ui/styles/getMuiTheme';

const appTheme = getMuiTheme({
  appBar: {
    height: 56,
    color: deepPurple500,
    showMenuIconButton: false
  },
  floatingActionButton:{
    color: lightBlue500,
  }
});

export default appTheme;
