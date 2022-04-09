namespace Gui
{
    partial class MainViewForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.button1 = new System.Windows.Forms.Button();
            this.rallyGridView = new System.Windows.Forms.DataGridView();
            this.participantsGridView = new System.Windows.Forms.DataGridView();
            this.teamNamesForSearchComboBox = new System.Windows.Forms.ComboBox();
            this.rallyEngineCapacityInput = new System.Windows.Forms.TextBox();
            this.teamNameInput = new System.Windows.Forms.TextBox();
            this.participantNameInput = new System.Windows.Forms.TextBox();
            this.teamNameForParticipantSaveComboBox = new System.Windows.Forms.ComboBox();
            this.rallyComboBox = new System.Windows.Forms.ComboBox();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.label5 = new System.Windows.Forms.Label();
            this.label6 = new System.Windows.Forms.Label();
            this.saveRallyButton = new System.Windows.Forms.Button();
            this.saveTeamButton = new System.Windows.Forms.Button();
            this.label3 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.label7 = new System.Windows.Forms.Label();
            this.label8 = new System.Windows.Forms.Label();
            this.registerParticipantButton = new System.Windows.Forms.Button();
            this.teamAddExceptionLabel = new System.Windows.Forms.Label();
            this.rallyAddExceptionLabel = new System.Windows.Forms.Label();
            this.participantAddExceptionLabel = new System.Windows.Forms.Label();
            ((System.ComponentModel.ISupportInitialize)(this.rallyGridView)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.participantsGridView)).BeginInit();
            this.SuspendLayout();
            // 
            // button1
            // 
            this.button1.Font = new System.Drawing.Font("Segoe UI", 10.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point);
            this.button1.Location = new System.Drawing.Point(695, 12);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(93, 33);
            this.button1.TabIndex = 0;
            this.button1.Text = "Logout";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // rallyGridView
            // 
            this.rallyGridView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.rallyGridView.Location = new System.Drawing.Point(12, 117);
            this.rallyGridView.Name = "rallyGridView";
            this.rallyGridView.RowHeadersWidth = 51;
            this.rallyGridView.RowTemplate.Height = 29;
            this.rallyGridView.Size = new System.Drawing.Size(405, 188);
            this.rallyGridView.TabIndex = 1;
            // 
            // participantsGridView
            // 
            this.participantsGridView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.participantsGridView.Location = new System.Drawing.Point(436, 117);
            this.participantsGridView.Name = "participantsGridView";
            this.participantsGridView.RowHeadersWidth = 51;
            this.participantsGridView.RowTemplate.Height = 29;
            this.participantsGridView.Size = new System.Drawing.Size(410, 188);
            this.participantsGridView.TabIndex = 2;
            // 
            // teamNamesForSearchComboBox
            // 
            this.teamNamesForSearchComboBox.FormattingEnabled = true;
            this.teamNamesForSearchComboBox.Location = new System.Drawing.Point(427, 83);
            this.teamNamesForSearchComboBox.Name = "teamNamesForSearchComboBox";
            this.teamNamesForSearchComboBox.Size = new System.Drawing.Size(151, 28);
            this.teamNamesForSearchComboBox.TabIndex = 3;
            this.teamNamesForSearchComboBox.SelectedIndexChanged += new System.EventHandler(this.teamNamesForSearchComboBox_SelectedIndexChanged);
            // 
            // rallyEngineCapacityInput
            // 
            this.rallyEngineCapacityInput.Location = new System.Drawing.Point(128, 340);
            this.rallyEngineCapacityInput.Name = "rallyEngineCapacityInput";
            this.rallyEngineCapacityInput.Size = new System.Drawing.Size(189, 27);
            this.rallyEngineCapacityInput.TabIndex = 4;
            // 
            // teamNameInput
            // 
            this.teamNameInput.Location = new System.Drawing.Point(128, 444);
            this.teamNameInput.Name = "teamNameInput";
            this.teamNameInput.Size = new System.Drawing.Size(189, 27);
            this.teamNameInput.TabIndex = 5;
            // 
            // participantNameInput
            // 
            this.participantNameInput.Location = new System.Drawing.Point(555, 347);
            this.participantNameInput.Name = "participantNameInput";
            this.participantNameInput.Size = new System.Drawing.Size(176, 27);
            this.participantNameInput.TabIndex = 6;
            // 
            // teamNameForParticipantSaveComboBox
            // 
            this.teamNameForParticipantSaveComboBox.FormattingEnabled = true;
            this.teamNameForParticipantSaveComboBox.Location = new System.Drawing.Point(555, 380);
            this.teamNameForParticipantSaveComboBox.Name = "teamNameForParticipantSaveComboBox";
            this.teamNameForParticipantSaveComboBox.Size = new System.Drawing.Size(176, 28);
            this.teamNameForParticipantSaveComboBox.TabIndex = 7;
            // 
            // rallyComboBox
            // 
            this.rallyComboBox.FormattingEnabled = true;
            this.rallyComboBox.Location = new System.Drawing.Point(555, 414);
            this.rallyComboBox.Name = "rallyComboBox";
            this.rallyComboBox.Size = new System.Drawing.Size(176, 28);
            this.rallyComboBox.TabIndex = 8;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(35, 343);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(87, 20);
            this.label1.TabIndex = 9;
            this.label1.Text = "Engine Cap:";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(30, 447);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(92, 20);
            this.label2.TabIndex = 10;
            this.label2.Text = "Team Name:";
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Location = new System.Drawing.Point(127, 317);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(76, 20);
            this.label5.TabIndex = 13;
            this.label5.Text = "Add Rally:";
            // 
            // label6
            // 
            this.label6.AutoSize = true;
            this.label6.Location = new System.Drawing.Point(127, 422);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(80, 20);
            this.label6.TabIndex = 14;
            this.label6.Text = "Add Team:";
            // 
            // saveRallyButton
            // 
            this.saveRallyButton.Location = new System.Drawing.Point(323, 338);
            this.saveRallyButton.Name = "saveRallyButton";
            this.saveRallyButton.Size = new System.Drawing.Size(94, 29);
            this.saveRallyButton.TabIndex = 15;
            this.saveRallyButton.Text = "Save";
            this.saveRallyButton.UseVisualStyleBackColor = true;
            this.saveRallyButton.Click += new System.EventHandler(this.saveRallyButton_Click);
            // 
            // saveTeamButton
            // 
            this.saveTeamButton.Location = new System.Drawing.Point(323, 444);
            this.saveTeamButton.Name = "saveTeamButton";
            this.saveTeamButton.Size = new System.Drawing.Size(94, 29);
            this.saveTeamButton.TabIndex = 16;
            this.saveTeamButton.Text = "Save";
            this.saveTeamButton.UseVisualStyleBackColor = true;
            this.saveTeamButton.Click += new System.EventHandler(this.saveTeamButton_Click);
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(555, 324);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(116, 20);
            this.label3.TabIndex = 17;
            this.label3.Text = "Add participant:";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(499, 354);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(52, 20);
            this.label4.TabIndex = 18;
            this.label4.Text = "Name:";
            // 
            // label7
            // 
            this.label7.AutoSize = true;
            this.label7.Location = new System.Drawing.Point(501, 388);
            this.label7.Name = "label7";
            this.label7.Size = new System.Drawing.Size(48, 20);
            this.label7.TabIndex = 19;
            this.label7.Text = "Team:";
            // 
            // label8
            // 
            this.label8.AutoSize = true;
            this.label8.Location = new System.Drawing.Point(501, 422);
            this.label8.Name = "label8";
            this.label8.Size = new System.Drawing.Size(44, 20);
            this.label8.TabIndex = 20;
            this.label8.Text = "Rally:";
            // 
            // registerParticipantButton
            // 
            this.registerParticipantButton.Location = new System.Drawing.Point(595, 484);
            this.registerParticipantButton.Name = "registerParticipantButton";
            this.registerParticipantButton.Size = new System.Drawing.Size(94, 29);
            this.registerParticipantButton.TabIndex = 21;
            this.registerParticipantButton.Text = "Register";
            this.registerParticipantButton.UseVisualStyleBackColor = true;
            this.registerParticipantButton.Click += new System.EventHandler(this.registerParticipantButton_Click);
            // 
            // teamAddExceptionLabel
            // 
            this.teamAddExceptionLabel.AutoSize = true;
            this.teamAddExceptionLabel.ForeColor = System.Drawing.Color.Red;
            this.teamAddExceptionLabel.Location = new System.Drawing.Point(193, 474);
            this.teamAddExceptionLabel.Name = "teamAddExceptionLabel";
            this.teamAddExceptionLabel.Size = new System.Drawing.Size(50, 20);
            this.teamAddExceptionLabel.TabIndex = 12;
            this.teamAddExceptionLabel.Text = "label4";
            this.teamAddExceptionLabel.Visible = false;
            // 
            // rallyAddExceptionLabel
            // 
            this.rallyAddExceptionLabel.AutoSize = true;
            this.rallyAddExceptionLabel.ForeColor = System.Drawing.Color.Red;
            this.rallyAddExceptionLabel.Location = new System.Drawing.Point(193, 370);
            this.rallyAddExceptionLabel.Name = "rallyAddExceptionLabel";
            this.rallyAddExceptionLabel.Size = new System.Drawing.Size(50, 20);
            this.rallyAddExceptionLabel.TabIndex = 11;
            this.rallyAddExceptionLabel.Text = "label3";
            this.rallyAddExceptionLabel.Visible = false;
            // 
            // participantAddExceptionLabel
            // 
            this.participantAddExceptionLabel.AutoSize = true;
            this.participantAddExceptionLabel.ForeColor = System.Drawing.Color.Red;
            this.participantAddExceptionLabel.Location = new System.Drawing.Point(621, 448);
            this.participantAddExceptionLabel.Name = "participantAddExceptionLabel";
            this.participantAddExceptionLabel.Size = new System.Drawing.Size(50, 20);
            this.participantAddExceptionLabel.TabIndex = 22;
            this.participantAddExceptionLabel.Text = "label9";
            this.participantAddExceptionLabel.Visible = false;
            // 
            // MainViewForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 20F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(858, 525);
            this.Controls.Add(this.participantAddExceptionLabel);
            this.Controls.Add(this.registerParticipantButton);
            this.Controls.Add(this.label8);
            this.Controls.Add(this.label7);
            this.Controls.Add(this.label4);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.saveTeamButton);
            this.Controls.Add(this.saveRallyButton);
            this.Controls.Add(this.label6);
            this.Controls.Add(this.label5);
            this.Controls.Add(this.teamAddExceptionLabel);
            this.Controls.Add(this.rallyAddExceptionLabel);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.rallyComboBox);
            this.Controls.Add(this.teamNameForParticipantSaveComboBox);
            this.Controls.Add(this.participantNameInput);
            this.Controls.Add(this.teamNameInput);
            this.Controls.Add(this.rallyEngineCapacityInput);
            this.Controls.Add(this.teamNamesForSearchComboBox);
            this.Controls.Add(this.participantsGridView);
            this.Controls.Add(this.rallyGridView);
            this.Controls.Add(this.button1);
            this.Name = "MainViewForm";
            this.Text = "MainViewForm";
            this.FormClosed += new System.Windows.Forms.FormClosedEventHandler(this.MainViewForm_FormClosed);
            this.Load += new System.EventHandler(this.MainViewForm_Load);
            ((System.ComponentModel.ISupportInitialize)(this.rallyGridView)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.participantsGridView)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private Button button1;
        private DataGridView rallyGridView;
        private DataGridView participantsGridView;
        private ComboBox teamNamesForSearchComboBox;
        private TextBox rallyEngineCapacityInput;
        private TextBox teamNameInput;
        private TextBox participantNameInput;
        private ComboBox teamNameForParticipantSaveComboBox;
        private ComboBox rallyComboBox;
        private Label label1;
        private Label label2;
        private Label label5;
        private Label label6;
        private Button saveRallyButton;
        private Button saveTeamButton;
        private Label label3;
        private Label label4;
        private Label label7;
        private Label label8;
        private Button registerParticipantButton;
        private Label teamAddExceptionLabel;
        private Label rallyAddExceptionLabel;
        private Label participantAddExceptionLabel;
    }
}