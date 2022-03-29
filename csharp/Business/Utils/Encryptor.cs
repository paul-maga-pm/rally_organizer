using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;

namespace Business.Utils
{
    public class Encryptor
    {
        public String GetHashForPassword(string password)
        {
            using (SHA512 sha = SHA512.Create())
            {
                byte[] hash = sha.ComputeHash(Encoding.Unicode.GetBytes(password));
                return Convert.ToBase64String(hash);
            }
        }

        public Boolean Authenticate(String password, String hashedPassword)
        {
            return GetHashForPassword(password).Equals(hashedPassword);
        }
    }
}
