namespace Gui
{
    partial class LoginForm
    {
        /// <summary>
        ///  Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        ///  Clean up any resources being used.
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
        ///  Required method for Designer support - do not modify
        ///  the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.label1 = new System.Windows.Forms.Label();
            this.usernameTextBox = new System.Windows.Forms.TextBox();
            this.label2 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.passwordTextBox = new System.Windows.Forms.TextBox();
            this.loginButton = new System.Windows.Forms.Button();
            this.label4 = new System.Windows.Forms.Label();
            this.loginExceptionLabel = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("Segoe UI", 28.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point);
            this.label1.Location = new System.Drawing.Point(77, 68);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(157, 62);
            this.label1.TabIndex = 0;
            this.label1.Text = "Rallies";
            // 
            // usernameTextBox
            // 
            this.usernameTextBox.Location = new System.Drawing.Point(96, 190);
            this.usernameTextBox.Name = "usernameTextBox";
            this.usernameTextBox.Size = new System.Drawing.Size(167, 27);
            this.usernameTextBox.TabIndex = 1;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(12, 193);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(78, 20);
            this.label2.TabIndex = 2;
            this.label2.Text = "Username:";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(12, 257);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(73, 20);
            this.label3.TabIndex = 3;
            this.label3.Text = "Password:";
            // 
            // passwordTextBox
            // 
            this.passwordTextBox.Location = new System.Drawing.Point(96, 257);
            this.passwordTextBox.Name = "passwordTextBox";
            this.passwordTextBox.PasswordChar = '*';
            this.passwordTextBox.Size = new System.Drawing.Size(167, 27);
            this.passwordTextBox.TabIndex = 4;
            // 
            // loginButton
            // 
            this.loginButton.Location = new System.Drawing.Point(96, 354);
            this.loginButton.Name = "loginButton";
            this.loginButton.Size = new System.Drawing.Size(94, 29);
            this.loginButton.TabIndex = 5;
            this.loginButton.Text = "Login";
            this.loginButton.UseVisualStyleBackColor = true;
            this.loginButton.Click += new System.EventHandler(this.loginButton_Click);
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(0, 0);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(50, 20);
            this.label4.TabIndex = 6;
            this.label4.Text = "label4";
            // 
            // loginExceptionLabel
            // 
            this.loginExceptionLabel.AutoSize = true;
            this.loginExceptionLabel.Location = new System.Drawing.Point(116, 306);
            this.loginExceptionLabel.Name = "loginExceptionLabel";
            this.loginExceptionLabel.Size = new System.Drawing.Size(50, 20);
            this.loginExceptionLabel.TabIndex = 7;
            this.loginExceptionLabel.Text = "label5";
            this.loginExceptionLabel.Visible = false;
            // 
            // LoginForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 20F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(320, 416);
            this.Controls.Add(this.loginExceptionLabel);
            this.Controls.Add(this.label4);
            this.Controls.Add(this.loginButton);
            this.Controls.Add(this.passwordTextBox);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.usernameTextBox);
            this.Controls.Add(this.label1);
            this.Name = "LoginForm";
            this.Text = "Rallies";
            this.FormClosed += new System.Windows.Forms.FormClosedEventHandler(this.LoginForm_FormClosed);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private Label label1;
        private TextBox usernameTextBox;
        private Label label2;
        private Label label3;
        private TextBox passwordTextBox;
        private Button loginButton;
        private Label label4;
        private Label loginExceptionLabel;
    }
}