using Domain.Models;
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



        private BindingList<Rally> rallyObservableCollection = new BindingList<Rally>();
        private BindingList<Team> teamObservableCollection = new BindingList<Team>();
        private BindingList<Participant> participantObservableCollection = new BindingList<Participant>();

        public MainViewForm(LoginForm loginForm)
        {
            this.loginForm = loginForm;
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
            participantObservableCollection.Clear();
            foreach(var participant in Services.ParticipantService.GetAllMembersOfTeam(teamName))
                participantObservableCollection.Add(participant);
        }

        private void LoadRallies()
        {
            rallyObservableCollection.Clear();
            foreach (var rally in Services.RallyService.GetAllRallies())
                rallyObservableCollection.Add(rally);
        }

        private void LoadTeams()
        {
            teamObservableCollection.Clear();
            foreach (var team in Services.TeamService.GetAllTeams())
                teamObservableCollection.Add(team);
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
            try
            {
                rallyAddExceptionLabel.Text = "";
                rallyAddExceptionLabel.Visible = true;
                int engineCapacity = Convert.ToInt32(rallyEngineCapacityInput.Text);

                if (engineCapacity < 50 || engineCapacity > 2000)
                {
                    rallyAddExceptionLabel.Text = "Capacity must be between 50 and 2000";
                    return;
                }
                Rally addedRally = Services.RallyService.AddRally(engineCapacity);

                if (!rallyObservableCollection.Contains(addedRally))
                {
                    rallyObservableCollection.Add(addedRally);
                }
                rallyAddExceptionLabel.Text = "";
                rallyAddExceptionLabel.Visible = false;
            }
            catch (Exception ex)
            {
                rallyAddExceptionLabel.Text = "Invalid numerical value!";
            }
        }

        private void saveTeamButton_Click(object sender, EventArgs e)
        {
            String teamName = teamNameInput.Text.Trim();
            teamAddExceptionLabel.Text = "";
            teamAddExceptionLabel.Visible = true;
            if (teamName.Equals(""))
            {
                teamAddExceptionLabel.Text = "Team name can't be empty!";
                return;
            }
            else if (Services.TeamService.FindTeamByName(teamName) != null)
            {
                teamAddExceptionLabel.Text = teamName + " team is already registered!";
                return;
            }
            else
            {
                teamAddExceptionLabel.Text = "";
                Team addedTeam = Services.TeamService.AddTeam(teamName);
                teamObservableCollection.Add(addedTeam);

            }
            teamAddExceptionLabel.Visible = false;
        }

        private void registerParticipantButton_Click(object sender, EventArgs e)
        {
            String participantName = participantNameInput.Text.Trim();
            participantAddExceptionLabel.Text = "";
            participantAddExceptionLabel.Visible = true;

            if (participantName.Equals(""))
                participantAddExceptionLabel.Text = "Participant name can't be empty!";
            else
            {
                Team selectedTeam = teamNameForParticipantSaveComboBox.SelectedItem as Team;
                if (selectedTeam == null)
                {
                    participantAddExceptionLabel.Text = "Select team!";
                    return ;
                }

                Rally selectedRally = rallyComboBox.SelectedItem as Rally;
                if (selectedRally == null)
                {
                    participantAddExceptionLabel.Text = "Select rally!";
                    return;
                }

                if (Services.ParticipantService.GetParticipantByName(participantName) != null)
                {
                    participantAddExceptionLabel.Text = participantName + " " + " is already registered!";
                    return;
                }

                participantAddExceptionLabel.Text = "";

                var selectedTeamFromSearch = teamNamesForSearchComboBox.SelectedItem as Team;
                if (selectedTeamFromSearch != null && selectedTeamFromSearch.TeamName.Equals(selectedTeam.TeamName))
                    participantObservableCollection.Add(Services.ParticipantService.AddParticipant(selectedTeam, selectedRally, participantName));
                LoadRallies();
            }
        }
    }
}
