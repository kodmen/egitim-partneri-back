import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BolumComponent } from './list/bolum.component';
import { BolumDetailComponent } from './detail/bolum-detail.component';
import { BolumUpdateComponent } from './update/bolum-update.component';
import { BolumDeleteDialogComponent } from './delete/bolum-delete-dialog.component';
import { BolumRoutingModule } from './route/bolum-routing.module';

@NgModule({
  imports: [SharedModule, BolumRoutingModule],
  declarations: [BolumComponent, BolumDetailComponent, BolumUpdateComponent, BolumDeleteDialogComponent],
  entryComponents: [BolumDeleteDialogComponent],
})
export class BolumModule {}
