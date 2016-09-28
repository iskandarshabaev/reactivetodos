import React, { Component, PropTypes } from 'react';
import { Tasks } from '../api/tasks.js';
import { Meteor } from 'meteor/meteor';
import classnames from 'classnames';
import {Card, CardActions, CardTitle, CardText} from 'material-ui/Card';
import FlatButton from 'material-ui/FlatButton';
import {pink500} from 'material-ui/styles/colors';

// Task component - represents a single todo item
export default class Task extends Component {
  toggleChecked() {
    // Set the checked property to the opposite of its current value
    Meteor.call('tasks.setChecked', this.props.task._id, !this.props.task.checked);
  }

  deleteThisTask() {
    Meteor.call('tasks.remove', this.props.task._id);
  }

  togglePrivate() {
      Meteor.call('tasks.setPrivate', this.props.task._id, ! this.props.task.private);
    }

  render() {
    // Give tasks a different className when they are checked off,
    // so that we can style them nicely in CSS
    const taskClassName = classnames({
      checked: this.props.task.checked,
      private: this.props.task.private,
    });

    return (
      <li className={taskClassName}>
      <Card
        //style= {{background:pink500}}
      >
        <CardTitle
          title={this.props.task.title}
          //titleColor="#FFFFFF"
        />
        <CardText>
          {this.props.task.content}
        </CardText>
        <CardActions>
          <FlatButton label="Action1" />
          <FlatButton label="delete" onClick={this.deleteThisTask.bind(this)}/>
        </CardActions>
      </Card>
      </li>
      /*
        <input
          type="checkbox"
          readOnly
          checked={this.props.task.checked}
          onClick={this.toggleChecked.bind(this)}
        />

        { this.props.showPrivateButton ? (
          <button className="toggle-private" onClick={this.togglePrivate.bind(this)}>
            { this.props.task.private ? 'Private' : 'Public' }
          </button>
        ) : ''}
        */

    );
  }
}

Task.propTypes = {
  // This component gets the task to display through a React prop.
  // We can use propTypes to indicate it is required
  task: PropTypes.object.isRequired,
  showPrivateButton: React.PropTypes.bool.isRequired,
};
