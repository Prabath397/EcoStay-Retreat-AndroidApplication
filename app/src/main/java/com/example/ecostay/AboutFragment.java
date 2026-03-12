package com.example.ecostay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        TextView tvGreenInitiatives = view.findViewById(R.id.tvGreenInitiatives);
        TextView tvSustainability = view.findViewById(R.id.tvSustainability);
        TextView tvContact = view.findViewById(R.id.tvContact);

        tvGreenInitiatives.setText("🌱 Our Green Initiatives:\n\n" +
                "• 100% Solar Powered Resort\n" +
                "• Zero Waste Policy\n" +
                "• Organic Farm-to-Table Dining\n" +
                "• Rainwater Harvesting\n" +
                "• Electric Vehicle Charging Stations");

        tvSustainability.setText("🏔️ Sustainability Practices:\n\n" +
                "• Locally sourced materials for construction\n" +
                "• Native landscaping to preserve ecosystem\n" +
                "• Energy-efficient appliances throughout\n" +
                "• Composting program for organic waste\n" +
                "• Educational workshops on sustainability");

        tvContact.setText("📞 Contact Information:\n\n" +
                "📍 Address: 123 Old Galle Road, Colombo\n" +
                "📧 Email: info@ecostayretreat.com\n" +
                "📱 Phone: +94 11 123 4567\n" +
                "🌐 Website: www.ecostayretreat.com");

        return view;
    }
}