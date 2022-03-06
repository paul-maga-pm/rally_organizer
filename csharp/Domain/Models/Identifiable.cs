namespace Domain.Models
{
    public class Identifiable <ID>
    {
         public ID? Id { get; set; }

        public override bool Equals(object? obj)
        {
            return obj is Identifiable<ID> identifiable &&
                   EqualityComparer<ID?>.Default.Equals(Id, identifiable.Id);
        }

        public override int GetHashCode()
        {
            return HashCode.Combine(Id);
        }
    }
}