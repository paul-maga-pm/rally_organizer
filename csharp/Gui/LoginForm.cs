using Business.Exception;
using Business.Services.Api;
using Exceptions;

namespace Gui
{
    public partial class LoginForm : Form
    {
        private IRallyApplicationServices _services;
        private MainViewForm mainViewForm;
        public LoginForm(IRallyApplicationServices services)
        {
            _services = services;
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
                    _services.LoginUser(username, password);
                    ShowMainView();
                    loginExceptionLabel.Text = "";
                }
                catch (AuthenticationException ex)
                {
                    loginExceptionLabel.Text = ex.Message;
                    loginExceptionLabel.Visible = true;
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
                mainViewForm = new MainViewForm(this, _services);
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