using System.Runtime.Serialization;

namespace Exceptions
{
    public class ExceptionBaseClass : Exception
    {
        public ExceptionBaseClass()
        {
        }

        public ExceptionBaseClass(string? message) : base(message)
        {
        }

        public ExceptionBaseClass(string? message, Exception? innerException) : base(message, innerException)
        {
        }

        protected ExceptionBaseClass(SerializationInfo info, StreamingContext context) : base(info, context)
        {
        }


    }
}