import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {Button, Form, FormGroup, Input, Label, Modal, ModalBody, ModalHeader} from 'reactstrap';

class NoteCreatingForm extends Component {
    static propsTypes = {
        isFormOpen: PropTypes.bool.isRequired
    };

    state = {
        noteSubject: '',
        noteBody: '',
    };
    onChange = ({ target: { value, name } }) => this.setState({
            ...this.state,
            [name]: value,
        });

    save = () => {
        const {noteSubject, noteBody} = this.state;
        this.setState({noteSubject: '', noteBody: ''});
        this.props.onSubmit({
            customerId: this.props.customerId,
            noteSubject,
            noteBody,
        });
    };

    cancel = () => {
        this.setState({noteSubject: '', noteBody: ''});
        this.props.onCancel();
    };

    render() {
        const {isFormOpen} = this.props;
        const {noteSubject, noteBody} = this.state;

        return (
            <Modal isOpen={isFormOpen}>
                <ModalHeader>Note edit</ModalHeader>
                <ModalBody>
                    <Form className="text-primary">
                        <FormGroup>
                            <Label for="noteSubject_new">Subject</Label>
                            <Input type="text" name="noteSubject" id="noteSubject_new" placeholder="Please enter a subject longer than 5 characters" value={noteSubject} onChange={this.onChange}/>
                        </FormGroup>
                        <FormGroup>
                            <Label for="noteBody_new">Content</Label>
                            <Input type="textarea" name="noteBody" id="noteBody_new" placeholder="Please enter some notes" value={noteBody} onChange={this.onChange} />
                        </FormGroup>

                        <Button onClick={this.save} className="float-right ml-2">Submit</Button>
                        <Button onClick={this.cancel} className="float-right">Cancel</Button>
                    </Form>
                </ModalBody>
            </Modal>
        );
    }
}

export default NoteCreatingForm;
