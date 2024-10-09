



class HostOfMemberNoHost {
    
    static class MemberNoHost {}
}

class HostOfMemberMissingHost {
    
    static class MemberMissingHost {}
}

class HostOfMemberNotInstanceHost {
    
    static class MemberNotInstanceHost {
        Object[] oa; 
    }
}

class HostOfMemberNotOurHost {
    
    static class MemberNotOurHost {}
}

class HostOfMemberMalformedHost {
    
    static class MemberMalformedHost {}
}



class HostWithSelfMember {
    static class Member {}
}


class HostWithDuplicateMembers {
    static class Member1 {}
    static interface Member2 {}
}
