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

        public override bool Equals(object? obj)
        {
            return obj is User user &&
                   base.Equals(obj) &&
                   Id == user.Id &&
                   UserName == user.UserName &&
                   Password == user.Password;
        }

        public override int GetHashCode()
        {
            return HashCode.Combine(base.GetHashCode(), Id, UserName, Password);
        }

        public override string? ToString()
        {
            return "" + "Id: " + Id + ", Username: " + UserName + ", Password: " + Password;
        }
    }
}
