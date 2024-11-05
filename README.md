# Appointment Manager Project

## Overview
This project is a simple Appointment Manager application that allows users to create, delete, and check appointments. It is designed as a standalone Java application with a user-friendly graphical user interface (GUI) built using Java Swing.

## Features
- **Add Appointment**: Users can add new appointments by providing a start date, end date, and a description.
- **Delete Appointment**: Users can select and delete an existing appointment from the list.
- **Check Appointment**: Users can check if an appointment occurs on a specific date.

## Current Limitations & Future Improvements
- **Check Appointment Improvement**: The current implementation allows users to check for appointments but does not save appointments persistently. In the future, appointments will be saved to a database to ensure data is retained even after the application is closed.
- **Better UI Design**: The current UI is functional but basic. We plan to improve the overall design to make it more visually appealing and user-friendly.
- **Persistent Storage**: Currently, appointments are not saved when the application is closed. Future versions will include database integration to allow for persistent appointment storage.

## Role Distribution
- **UI Designer**: An Hoang is responsible for creating the user interface and will improve the design in future versions.
- **Programmer**: Kanan Ibadzade implemented core features such as adding, deleting, and checking appointments, with assistance from An Hoang on parts of `Main.java` and `Appointment.java`.
- **Tester**: Efe Bahadir Gur developed and ran JUnit tests to ensure the `occursOn` method functions correctly and helped with `AppointmentTest.java`.
