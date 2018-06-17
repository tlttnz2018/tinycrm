import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {Button, Form, FormGroup, Input, Label} from 'reactstrap';

class NoteEditingForm extends Component {
    static propsTypes = {
        index: PropTypes.number.isRequired
    };

    constructor(props) {
        super(props);
        this.state = {
            ...props.note
        }
    }
    onChange = ({ target: { name, value } }) => this.setState({
        ...this.state,
        [name]: value
    });

    save = (e) => {
        e.preventDefault();
        const {
            customerId,
            note: {
                id
            },
            onFinishEdit,
        } = this.props;
        const {noteSubject, noteBody} = this.state;
        onFinishEdit({
            customerId,
            id,
            noteSubject,
            noteBody,
        });
    };

    cancel = () => this.props.onFinishEdit();

    render() {
        const {index} = this.props;
        const {noteBody, noteSubject} = this.state;
        return (
            <Form className="pb-2 mt-4 mb-5 text-primary">
                <FormGroup>
                    <Label for={`noteSubject_${index}`}>Subject</Label>
                    <Input type="text" name="noteSubject" id={`noteSubject_${index}`} placeholder="Please enter a subject longer than 5 characters" value={noteSubject} onChange={this.onChange}/>
                </FormGroup>
                <FormGroup>
                    <Label for={`noteBody_${index}`}>Content</Label>
                    <Input type="textarea" name="noteBody" id={`noteBody_${index}`} placeholder="Please enter some notes" value={noteBody} onChange={this.onChange} />
                </FormGroup>

                <Button onClick={this.save} className="float-right ml-2">Submit</Button>
                <Button onClick={this.cancel} className="float-right">Cancel</Button>
            </Form>
        );
    }
}

export default NoteEditingForm;
