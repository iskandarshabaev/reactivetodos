import React, { Component, PropTypes } from 'react';
import ReactDOM from 'react-dom';
import { Meteor } from 'meteor/meteor';
import { createContainer } from 'meteor/react-meteor-data';

import { Tasks } from '../api/tasks.js';

import Task from './Task.jsx';
import AccountsUIWrapper from './AccountsUIWrapper.jsx';

import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import AppBar from './AppBar';
import appTheme from './AppTheme';
import FloatingActionButton from 'material-ui/FloatingActionButton';
import ContentAdd from 'material-ui/svg-icons/content/add';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';
import TextField from 'material-ui/TextField';
// App component - represents the whole app
class App extends Component {
  constructor(props) {
    super(props);

    this.state = {
      open: false,
      hideCompleted: false,
    };
    this.handleOpen = this.handleOpen.bind(this);
    this.handleClose = this.handleClose.bind(this);
  }

  handleSubmit(event) {
    event.preventDefault();
    const title = ReactDOM.findDOMNode(this.refs.titleInput).value.trim();
    const content = ReactDOM.findDOMNode(this.refs.textInput).value.trim();
    Meteor.call('tasks.insert', title, content);
    ReactDOM.findDOMNode(this.refs.textInput).value = '';
  }

  toggleHideCompleted() {
    this.setState({
      hideCompleted: !this.state.hideCompleted,
    });
  }

  handleOpen() {
    this.setState({
      open: true,
    });
  }

  handleClose() {
    this.setState({
      open: false,
    });
  }

  handleSubmit2(event) {
    event.preventDefault();
    const title = this.refs.title11.getValue().trim();
    const content = this.refs.content11.getValue().trim();
    Meteor.call('tasks.insert', title, content);
    //ReactDOM.findDOMNode(this.refs.textInput).value = '';
    this.setState({
      open: false,
    });
  }

  renderTasks() {
    let filteredTasks = this.props.tasks;
    if (this.state.hideCompleted) {
      filteredTasks = filteredTasks.filter(task => !task.checked);
    }
    return filteredTasks.map((task) => {
      const currentUserId = this.props.currentUser && this.props.currentUser._id;
      const showPrivateButton = task.owner === currentUserId;

      return (

        <Task
          key={task._id}
          task={task}
          showPrivateButton={showPrivateButton}
        />

        /*
        <CardExampleExpandable
          key={task._id}
        />
        */
      );
    });
  }

  render() {
    const actions = [
      <FlatButton
        label="Cancel"
        primary={true}
        onTouchTap={this.handleClose}
      />,
      <FlatButton
        label="Submit"
        primary={true}
        keyboardFocused={true}
        onTouchTap={
          this.handleSubmit2.bind(this)
        }
      />,
    ];
    return (
      <MuiThemeProvider muiTheme={appTheme}>
      <div>
      <AppBar />
      <div className="container">

        <header>
          <h1>Todo List</h1>

          <AccountsUIWrapper />

        </header>

        <ul>
          {this.renderTasks()}
        </ul>
      </div>
      <FloatingActionButton className="fab"
      onTouchTap={this.handleOpen}>
      <ContentAdd />
    </FloatingActionButton>
    <Dialog
          title="Dialog With Actions"
          actions={actions}
          modal={false}
          contentStyle={
            {
              position: 'absolute',
              left: '50%',
              top: '50%',
              transform: 'translate(-50%, -50%)'
            }
          }
          open={this.state.open}
          onRequestClose={this.handleClose}
        >
        <TextField
          ref="title11"
          hintText="Title"
          floatingLabelText="Title"
          fullWidth={true}
        /><br />
        <TextField
          ref="content11"
          hintText="Content"
          multiLine={true}
          rows={2}
          fullWidth={true}
          floatingLabelText="Content"
        /><br />
        </Dialog>
      </div>
      </MuiThemeProvider>
    );
  }
}

App.propTypes = {
  tasks: PropTypes.array.isRequired,
  incompleteCount: PropTypes.number.isRequired,
  currentUser: PropTypes.object,
};

export default createContainer(() => {
  Meteor.subscribe('tasks');
  return {
    tasks: Tasks.find({}, { sort: { createdAt: -1 } }).fetch(),
    incompleteCount: Tasks.find({ checked: { $ne: true } }).count(),
    currentUser: Meteor.user(),
  };
}, App);
