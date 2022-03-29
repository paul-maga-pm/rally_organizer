using Business.Utils;
using Domain.Models;
using Repository.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Authentication;
using System.Text;
using System.Threading.Tasks;

namespace Business.Services
{
    public class UserService
    {
        private IUserRepository userRepository;
        private Encryptor encryptor = new Encryptor(); 

        public UserService(IUserRepository userRepository)
        {
            this.userRepository = userRepository;
        }

        public void LoginUser(String userName, String password)
        {
            var foundUser = userRepository.FindUserByUsername(userName);

            if (foundUser == null)
                throw new AuthenticationException("Username doesn't exist!");

            if (!encryptor.Authenticate(password, foundUser.Password))
                throw new AuthenticationException("Incorrect password!");
        }

        public void RegisterUser(String userName, String password)
        {
            if (userRepository.FindUserByUsername(userName) != null)
                throw new AuthenticationException("Username " + userName + " is already used!");

            String hashedPassword = encryptor.GetHashForPassword(password);
            User user = new User(userName, hashedPassword);
            userRepository.Add(user);
        }
    }
}
