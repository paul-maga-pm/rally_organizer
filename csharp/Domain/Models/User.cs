using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Domain.Models
{
    public class User: Identifiable<long>
    {
        public String UserName { get; set; }
        public String Password { get; set; }

        public User(string userName, string password)
        {
            UserName = userName;
            Password = password;
        }

     }
}
