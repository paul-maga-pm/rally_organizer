using Business.Services.Api;
using Domain.Models;
using Exceptions;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Drawing;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Gui
{

    public partial class MainViewForm : Form
    {
        private LoginForm loginForm;
        private IRallyApplicationServices _services;


        private ICollection<Rally> rallyObservableCollection = new BindingList<Rally>();
        private ICollection<Team> teamObservableCollection = new BindingList<Team>();
        private ICollection<Participant> participantObservableCollection = new BindingList<Participant>();

        public MainViewForm(LoginForm loginForm, IRallyApplicationServices services)
        {
            this.loginForm = loginForm;
            this._services = services;
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            loginForm.Show();
            this.Hide();
        }



        private void MainViewForm_Load(object sender, EventArgs e)
        {
            InitializeObservableLists();
            rallyGridView.DataSource = rallyObservableCollection;
            teamNamesForSearchComboBox.DataSource = teamObservableCollection;
            teamNameForParticipantSaveComboBox.DataSource = teamObservableCollection;
            rallyComboBox.DataSource = rallyObservableCollection;
            participantsGridView.DataSource = participantObservableCollection;
        }

        private void InitializeObservableLists()
        {
            LoadTeams();
            LoadRallies();
        }

        private void LoadMembersOfTeam(String teamName)
        {
            try
            {
                participantObservableCollection.Clear();
                foreach(Participant participant in _services.GetAllMembersOfTeam(teamName))
                    participantObservableCollection.Add(participant);
            }
            catch(ExceptionBaseClass exception)
            {
                MessageBox.Show(exception.Message);
            }
        }

        private void LoadRallies()
        {
            try
            {
                rallyObservableCollection.Clear();
                foreach (Rally rally in _services.GetAllRallies())
                    rallyObservableCollection.Add(rally);
            }
            catch(ExceptionBaseClass exception)
            {
                MessageBox.Show(exception.Message);
            }
        }

        private void LoadTeams()
        {
            try
            {
                teamObservableCollection.Clear();
                foreach(Team team in _services.GetAllTeams())
                    teamObservableCollection.Add(team);
            }
            catch(ExceptionBaseClass exception)
            {
                MessageBox.Show(exception.Message);
            }
        }

        private void MainViewForm_FormClosed(object sender, FormClosedEventArgs e)
        {
            if (loginForm != null)
            {
                loginForm.Dispose();
                loginForm.Close();
            }
        }

        private void teamNamesForSearchComboBox_SelectedIndexChanged(object sender, EventArgs e)
        {
            ComboBox comboBox = (ComboBox)sender;
            Team selectedValue = (Team)comboBox.SelectedItem;
            LoadMembersOfTeam(selectedValue.TeamName);
        }

        private void saveRallyButton_Click(object sender, EventArgs e)
        {
           
            rallyAddExceptionLabel.Text = "";
            rallyAddExceptionLabel.Visible = true;
            int engineCapacity;
            try
            {
                engineCapacity = Convert.ToInt32(rallyEngineCapacityInput.Text);
            }
            catch(FormatException)
            {
                rallyAddExceptionLabel.Text = "Invalid numerical value!";
                return;
            }

            if (engineCapacity < 50 || engineCapacity > 2000)
            {
                rallyAddExceptionLabel.Text = "Capacity must be between 50 and 2000";
                return;
            }

            Rally addedRally;
            try
            {
                addedRally = _services.AddRally(engineCapacity);

            }
            catch (ExceptionBaseClass exception)
            {
                MessageBox.Show(exception.Message);
                return;
            }
            if (!rallyObservableCollection.Contains(addedRally))
            {
                rallyObservableCollection.Add(addedRally);
            }
            rallyAddExceptionLabel.Text = "";
            rallyAddExceptionLabel.Visible = false;
            
        }

        private void saveTeamButton_Click(object sender, EventArgs e)
        {
            String teamName = teamNameInput.Text.Trim();
            teamAddExceptionLabel.Text = "";
            teamAddExceptionLabel.Visible = true;

            try
            {
                if (teamName.Equals(""))
                {
                    teamAddExceptionLabel.Text = "Team name can't be empty!";
                    return;
                }
                else if (_services.GetTeamByName(teamName) != null)
                {
                    teamAddExceptionLabel.Text = teamName + " team is already registered!";
                    return;
                }
                else
                {
                    teamAddExceptionLabel.Text = "";
                    Team addedTeam = _services.AddTeam(teamName);
                    teamObservableCollection.Add(addedTeam);

                }
                teamAddExceptionLabel.Visible = false;
            }
            catch(ExceptionBaseClass exception)
            {
                MessageBox.Show(exception.Message);
            }
        }

        private void registerParticipantButton_Click(object sender, EventArgs e)
        {
            String participantName = participantNameInput.Text.Trim();
            participantAddExceptionLabel.Text = "";
            participantAddExceptionLabel.Visible = true;

            try
            {
                if (participantName.Equals(""))
                    participantAddExceptionLabel.Text = "Participant name can't be empty!";
                else
                {
                    Team? selectedTeam = teamNameForParticipantSaveComboBox.SelectedItem as Team;
                    if (selectedTeam == null)
                    {
                        participantAddExceptionLabel.Text = "Select team!";
                        return;
                    }

                    Rally? selectedRally = rallyComboBox.SelectedItem as Rally;
                    if (selectedRally == null)
                    {
                        participantAddExceptionLabel.Text = "Select rally!";
                        return;
                    }

                    if (_services.GetParticipantByName(participantName) != null)
                    {
                        participantAddExceptionLabel.Text = participantName + " " + " is already registered!";
                        return;
                    }

                    participantAddExceptionLabel.Text = "";

                    var selectedTeamFromSearch = teamNamesForSearchComboBox.SelectedItem as Team;
                    if (selectedTeamFromSearch != null && selectedTeamFromSearch.TeamName.Equals(selectedTeam.TeamName))
                        participantObservableCollection.Add(_services.AddParticipant(selectedTeam, selectedRally, participantName));
                    LoadRallies();
                }
            }
            catch(ExceptionBaseClass exception)
            {
                MessageBox.Show(exception.Message);
            }
        }
    }
}
