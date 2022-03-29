using Business.Exception;
using Business.Services;
using Exceptions;

namespace Gui
{
    public partial class LoginForm : Form
    {
        private MainViewForm mainViewForm;
        public LoginForm()
        {
            InitializeComponent();
        }

        private void loginButton_Click(object sender, EventArgs e)
        {
            String username = usernameTextBox.Text.Trim();
            String password = passwordTextBox.Text.Trim();

            if (username != null && password != null)
            {
                try
                {
                    Services.UserService.LoginUser(username, password);

                    ShowMainView();
                }
                catch (AuthenticationException ex)
                {
                    loginExceptionLabel.Text = ex.Message;
                }
                catch(ExceptionBaseClass ex)
                {
                    MessageBox.Show(ex.Message);
                }
            }
        }

        private void ShowMainView()
        {
            if (mainViewForm == null)
            {
                mainViewForm = new MainViewForm(this);
            }
            mainViewForm.Show();
            this.Hide();
        }

        private void LoginForm_FormClosed(object sender, FormClosedEventArgs e)
        {
            if (mainViewForm != null)
            {
                mainViewForm.Dispose();
                mainViewForm.Close();
            }
        }
    }
}