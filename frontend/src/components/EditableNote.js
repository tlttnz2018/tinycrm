import React, {Component} from 'react';
import PropTypes from 'prop-types';
import NoteEditingForm from "./NoteEditingForm";
import Note from "./Note";

class EditableNote extends Component {
    static propsTypes = {
        index: PropTypes.number.isRequired,
        customerId: PropTypes.string.isRequired,
        node: PropTypes.object
    };

    state = {
        editNoteFormOpen: false,
    };

    startEditNote = () => this.setState({editNoteFormOpen: true});

    finishEditNote = (updateNote) => {
        this.setState({editNoteFormOpen: false});

        if(!!updateNote) {
            this.props.onUpdateNote(updateNote);
        }
    };

    render() {
        const {index, customerId, note} = this.props;

        if (this.state.editNoteFormOpen) {
            return (
                <NoteEditingForm key={index} index={index} customerId={customerId} note={note} onFinishEdit={this.finishEditNote}/>
            );
        } else {
            return (
                <Note key={index} customerId={customerId} note={note} onStartEdit={this.startEditNote}/>
            );
        }
    }
}

export default EditableNote;
