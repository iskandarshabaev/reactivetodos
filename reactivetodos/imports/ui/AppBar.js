import React from 'react';
import AppBar from 'material-ui/AppBar';
import IconButton from 'material-ui/IconButton';

const AppBarExampleIcon = () => (
  <AppBar
    title="TodoApp"
    iconElementLeft={<IconButton></IconButton>}
    style={{position: 'fixed'}}
  />
);

export default AppBarExampleIcon;
